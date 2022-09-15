package io.piveau.validating

import org.apache.jena.rdf.model.Model
import org.apache.jena.shacl.ShaclValidator

import org.slf4j.LoggerFactory


var useJena: Boolean = true

const val DEFAULT_SHAPES_VERSION: String = "idsbase"

private val validator = ShaclValidator.get()

fun validateModel(model: Model, shapesVersion: String = DEFAULT_SHAPES_VERSION): Model {
    val shapes = shapesVersion.findShapes()
    val report = validator.validate(shapes, model.graph)
    return report.model
}
