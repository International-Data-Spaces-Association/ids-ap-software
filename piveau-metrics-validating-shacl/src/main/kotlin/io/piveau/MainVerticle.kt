package io.piveau

import io.piveau.pipe.connector.PipeConnector
import io.piveau.utils.ConfigurableAssetHandler
import io.piveau.validating.DEFAULT_SHAPES_VERSION
import io.piveau.validating.ReportGenerationVerticle
import io.piveau.validating.ValidatingShaclVerticle
import io.vertx.config.ConfigRetriever
import io.vertx.config.ConfigRetrieverOptions
import io.vertx.config.ConfigStoreOptions
import io.vertx.core.DeploymentOptions
import io.vertx.core.Launcher
import io.vertx.core.eventbus.DeliveryOptions
import io.vertx.core.eventbus.ReplyException
import io.vertx.core.http.HttpMethod
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.client.WebClient
import io.vertx.ext.web.handler.CorsHandler
import io.vertx.ext.web.handler.StaticHandler
import io.vertx.ext.web.openapi.RouterBuilder
import io.vertx.kotlin.core.json.array
import io.vertx.kotlin.core.json.json
import io.vertx.kotlin.core.json.obj
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.coroutines.await
import org.slf4j.LoggerFactory

class MainVerticle : CoroutineVerticle() {

    private val log = LoggerFactory.getLogger(this.javaClass)

    override suspend fun start() {
        val options =
                ConfigStoreOptions().apply {
                    type = "env"
                    config = json {
                        obj(
                                "keys" to
                                        array(
                                                "PIVEAU_SHACL_VERTICLE_INSTANCES",
                                                "PIVEAU_SHACL_WORKER_POOL_SIZE",
                                                "PIVEAU_PIPE_ENDPOINT_PORT",
                                                "PIVEAU_LOGO_PATH",
                                                "PIVEAU_FAVICON_PATH",
                                                "PIVEAU_IMPRINT_URL",
                                                "PIVEAU_PRIVACY_URL"
                                        )
                        )
                    }
                }
        val config =
                ConfigRetriever.create(vertx, ConfigRetrieverOptions().addStore(options))
                        .config
                        .await()

        val instances =
                config.getInteger(
                        "PIVEAU_SHACL_VERTICLE_INSTANCES",
                        DeploymentOptions.DEFAULT_INSTANCES
                )
        val poolSize = config.getInteger("PIVEAU_SHACL_WORKER_POOL_SIZE", 20)

        vertx.deployVerticle(
                        ValidatingShaclVerticle::class.java,
                        DeploymentOptions()
                                .setInstances(instances)
                                .setWorkerPoolSize(poolSize)
                                .setWorker(true)
                )
                .await()

        vertx.deployVerticle(
                        ReportGenerationVerticle::class.java,
                        DeploymentOptions().setWorker(true)
                )
                .await()

        val connector = PipeConnector.create(vertx, DeploymentOptions()).await()
        log.info(
                "The service runs on port: " + config.getInteger("PIVEAU_PIPE_ENDPOINT_PORT", 8080)
        )
        connector.publishTo(ValidatingShaclVerticle.ADDRESS_PIPE)

        val builder = RouterBuilder.create(vertx, "webroot/openapi-shacl.yaml").await()
        builder.operation("validationReport").handler(::handleValidation)

        builder.rootHandler(
                CorsHandler.create("*").allowedMethods(setOf(HttpMethod.POST)).allowedHeader("*")
        )

        val router = builder.createRouter()

        val client = WebClient.create(vertx)
        router.route("/images/favicon")
                .handler(
                        ConfigurableAssetHandler(
                                config.getString(
                                        "PIVEAU_FAVICON_PATH",
                                        "webroot/images/favicon.png"
                                ),
                                client
                        )
                )
        router.route("/images/logo")
                .handler(
                        ConfigurableAssetHandler(
                                config.getString("PIVEAU_LOGO_PATH", "webroot/images/logo.png"),
                                client
                        )
                )

        router.route("/imprint").handler { context: RoutingContext ->
            context.redirect(config.getString("PIVEAU_IMPRINT_URL", "/shacl"))
        }
        router.route("/privacy").handler { context: RoutingContext ->
            context.redirect(config.getString("PIVEAU_PRIVACY_URL", "/shacl"))
        }

        router.route("/*").handler(StaticHandler.create().setIndexPage("index-shacl.html"))

        connector.subRouter("/shacl", router)
    }

    private fun handleValidation(context: RoutingContext) {
        val shapeModel = context.queryParam("shapeModel").firstOrNull() ?: DEFAULT_SHAPES_VERSION
        val message = JsonObject()
            .put("contentType", context.request().getHeader("Content-Type"))
            .put("content", context.bodyAsString)
            .put("acceptableContentType", context.acceptableContentType ?: "text/turtle")
            .put("shapeModel", shapeModel)

        vertx.eventBus().request<String>(
            ReportGenerationVerticle.ADDRESS_REPORT,
            message,
            DeliveryOptions().setSendTimeout(300000)
        ) {
            if (it.succeeded()) {
                context.response().putHeader("Content-Type", message.getString("acceptableContentType"))
                    .end(it.result().body())
            } else {
                it.cause().let { cause ->
                    if (cause is ReplyException) {
                        context.response().setStatusCode(cause.failureCode()).end(cause.message)
                    } else {
                        context.response().setStatusCode(500).end(cause.message)
                    }
                }
            }
        }
    }

}

fun main(args: Array<String>) {
    Launcher.executeCommand("run", *(args.plus(MainVerticle::class.java.name)))
}
