package io.piveau.validating

import io.piveau.vocabularies.readTurtleResource
import io.piveau.vocabularies.readXmlResource
import org.apache.jena.rdf.model.Model
import org.apache.jena.rdf.model.ModelFactory
import org.apache.jena.shacl.Shapes
import java.io.File

import org.slf4j.LoggerFactory

object Vocabularies {
    val model: Model = ModelFactory.createDefaultModel().apply {
        /*readXmlResource("ADMS_SKOS_v1.00.rdf")
        readXmlResource("continents-skos.rdf")
        readXmlResource("corporatebodies-skos.rdf")
        readXmlResource("countries-skos.rdf")
        readXmlResource("data-theme-skos.rdf")
        readXmlResource("filetypes-skos.rdf")
        readXmlResource("frequencies-skos.rdf")
        readXmlResource("languages-skos.rdf")
        readXmlResource("places-skos.rdf")*/
        readTurtleResource("rdf/shapes/ids-base/4.1.0/ids-ontology.ttl")
    }
}

/**
 * This is just a draft version for 2.1.1.
 * ToDo Needs to be updated after final release
 */
object DCATAP211 {
    val model: Model by lazy {
        ModelFactory.createDefaultModel().apply {
            readTurtleResource("rdf/shapes/dcat-ap/2.1.1/dcat-ap_2.1.1_shacl_shapes.ttl")
            readTurtleResource("rdf/shapes/dcat-ap/2.1.1/dcat-ap_2.1.1_shacl_shapes_recommended.ttl")
            readTurtleResource("rdf/shapes/dcat-ap/2.1.1/dcat-ap_2.1.1_shacl_range.ttl")
            readTurtleResource("rdf/shapes/dcat-ap/2.1.1/dcat-ap_2.1.1_shacl_deprecateduris.ttl")
            readTurtleResource("rdf/shapes/dcat-ap/2.1.1/dcat-ap_2.1.1_shacl_mdr-vocabularies.shape.ttl")
        }
    }
    val shapes: Shapes = Shapes.parse(model)
}

object DCATAP210 {
    val model: Model by lazy {
        ModelFactory.createDefaultModel().apply {
            readTurtleResource("rdf/shapes/dcat-ap/2.1.0/dcat-ap_2.1.0_shacl_shapes.ttl")
//            readTurtleResource("rdf/shapes/dcat-ap/2.1.0/dcat-ap_2.1.0_shacl_shapes_recommended.ttl")
//            readTurtleResource("rdf/shapes/dcat-ap/2.1.0/dcat-ap_2.1.0_shacl_range.ttl")
            readTurtleResource("rdf/shapes/dcat-ap/2.1.0/dcat-ap_2.1.0_shacl_deprecateduris.ttl")
//            readTurtleResource("rdf/shapes/dcat-ap/2.1.0/dcat-ap_2.1.0_shacl_mdr-vocabularies.shape.ttl")
        }
    }
    val shapes: Shapes = Shapes.parse(model)
}

object DCATAP201 {
    val model: Model by lazy {
        ModelFactory.createDefaultModel().apply {
            readTurtleResource("rdf/shapes/dcat-ap/2.0.1/dcat-ap_2.0.1_shacl_shapes.ttl")
            readTurtleResource("rdf/shapes/dcat-ap/2.0.1/dcat-ap_2.0.1_shacl_deprecateduris.ttl")
            readTurtleResource("rdf/shapes/dcat-ap/2.0.1/dcat-ap_2.0.1_shacl_mdr-vocabularies.shape.ttl")
        }
    }
    val shapes: Shapes = Shapes.parse(model)
}

object DCATAP200 {
    val model: Model by lazy {
        ModelFactory.createDefaultModel().apply {
            readTurtleResource("rdf/shapes/dcat-ap/2.0.0/dcat-ap_2.0.0_shacl_shapes.ttl")
            readTurtleResource("rdf/shapes/dcat-ap/2.0.0/dcat-ap_2.0.0_shacl_deprecateduris.ttl")
            readTurtleResource("rdf/shapes/dcat-ap/2.0.0/dcat-ap_2.0.0_shacl_mdr-vocabularies.shape_.ttl")
        }
    }
    val shapes: Shapes = Shapes.parse(model)
}

object DCATAP121 {
    val model: Model by lazy {
        ModelFactory.createDefaultModel().apply {
            readTurtleResource("rdf/shapes/dcat-ap/1.2.1-hotfix/dcat-ap_1.2.1_shacl_shapes.ttl")
            readTurtleResource("rdf/shapes/dcat-ap/1.2.1-hotfix/dcat-ap_1.2.1_shacl_mandatory-classes.shapes.ttl")
            readTurtleResource("rdf/shapes/dcat-ap/1.2.1-hotfix/dcat-ap_1.2.1_shacl_mdr-vocabularies.shape_.ttl")
        }
    }
    val shapes: Shapes = Shapes.parse(model)
}

object DCATAP121orig {
    val model: Model by lazy {
        ModelFactory.createDefaultModel().apply {
            readTurtleResource("rdf/shapes/dcat-ap/1.2.1/dcat-ap_1.2.1_shacl_shapes.ttl")
            readTurtleResource("rdf/shapes/dcat-ap/1.2.1/dcat-ap_1.2.1_shacl_mandatory-classes.shapes.ttl")
            readTurtleResource("rdf/shapes/dcat-ap/1.2.1/dcat-ap_1.2.1_shacl_mdr-vocabularies.shape.ttl")
        }
    }
    val shapes: Shapes = Shapes.parse(model)
}

object DCATAP12 {
    val model: Model by lazy {
        ModelFactory.createDefaultModel().apply {
            readTurtleResource("rdf/shapes/dcat-ap/1.2/dcat-ap.shapes.ttl")
            readTurtleResource("rdf/shapes/dcat-ap/1.2/dcat-ap-mandatory-classes.shapes.ttl")
            readTurtleResource("rdf/shapes/dcat-ap/1.2/dcat-ap-mdr-vocabularies.shapes.ttl")
        }
    }
    val shapes: Shapes = Shapes.parse(model)
}

object DCATAP11 {
    val model: Model by lazy {
        ModelFactory.createDefaultModel().apply {
            readTurtleResource("rdf/shapes/dcat-ap/1.1-201706/dcat-ap.shapes.ttl")
            readTurtleResource("rdf/shapes/dcat-ap/1.1-201706/dcat-ap-mandatory-classes.shapes.ttl")
            readTurtleResource("rdf/shapes/dcat-ap/1.1-201706/dcat-ap-mdr-vocabularies.shapes.ttl")
        }
    }
    val shapes: Shapes = Shapes.parse(model)
}

object DCATAP11orig {
    val model: Model by lazy {
        ModelFactory.createDefaultModel().apply {
            readTurtleResource("rdf/shapes/dcat-ap/1.1-201608/dcat-ap.shapes.ttl")
            readTurtleResource("rdf/shapes/dcat-ap/1.1-201608/dcat-ap-mandatory-classes.shapes.ttl")
            readTurtleResource("rdf/shapes/dcat-ap/1.1-201608/dcat-ap-mdr-vocabularies.shapes.ttl")
        }
    }
    val shapes: Shapes = Shapes.parse(model)
}

object DCATAPDE102 {
    val model: Model by lazy {
        ModelFactory.createDefaultModel().apply {
            readTurtleResource("rdf/shapes/dcat-ap.de/1.0.2/dcat-ap-de-shapes-specification.ttl")
            readTurtleResource("rdf/shapes/dcat-ap.de/1.0.2/dcat-ap-de-lists.ttl")
            readTurtleResource("rdf/shapes/dcat-ap.de/1.0.2/nal-lists.ttl")
        }
    }
    val shapes: Shapes = Shapes.parse(model)
}


object IDSBASE {
    val model: Model by lazy {
        val log = LoggerFactory.getLogger(this.javaClass)
        val model = ModelFactory.createDefaultModel()
        //log.info("Loading IDS Base")
        File("src/main/resources/rdf/shapes/ids-base/4.1.0/testing").walkTopDown().forEach {

            if (it.extension == "ttl") {
                log.info(it.absolutePath)
                model.read(it.absolutePath)
                log.info("Loaded " + it.absolutePath)
            }
        }
        model
    }

    val shapes: Shapes = Shapes.parse(model)
}

object IDSAPBASIC {
    val model: Model by lazy {
        ModelFactory.createDefaultModel().apply {
            readTurtleResource("rdf/shapes/ids-ap/basicIDSValidation-ExternalReferences.ttl")
        }
    }
    val shapes: Shapes = Shapes.parse(model)
}

object IDSAPEXT {
    val model: Model by lazy {
        ModelFactory.createDefaultModel().apply {
            readTurtleResource("rdf/shapes/ids-ap/extendedValidation-ExternalReferences.ttl")
        }
    }
    val shapes: Shapes = Shapes.parse(model)
}


fun String.findShapesModel(): Model = when (this) {
    "dcatap211" -> DCATAP211.model
    "dcatap210" -> DCATAP210.model
    "dcatap201" -> DCATAP201.model
    "dcatap200" -> DCATAP200.model
    "dcatap121" -> DCATAP121.model
    "dcatap121orig" -> DCATAP121orig.model
    "dcatap12" -> DCATAP12.model
    "dcatap11" -> DCATAP11.model
    "dcatap11orig" -> DCATAP11orig.model
    "dcatapde102" -> DCATAPDE102.model
    "idsapbasic" -> IDSAPBASIC.model
    "idsapext" -> IDSAPEXT.model
    "idsbase" -> IDSBASE.model
    else -> IDSBASE.model
}

fun String.findShapes(): Shapes = when (this) {
    "dcatap211" -> DCATAP211.shapes
    "dcatap210" -> DCATAP210.shapes
    "dcatap201" -> DCATAP201.shapes
    "dcatap200" -> DCATAP200.shapes
    "dcatap121" -> DCATAP121.shapes
    "dcatap121orig" -> DCATAP121orig.shapes
    "dcatap12" -> DCATAP12.shapes
    "dcatap11" -> DCATAP11.shapes
    "dcatap11orig" -> DCATAP11orig.shapes
    "dcatapde102" -> DCATAPDE102.shapes
    "idsapbasic" -> IDSAPBASIC.shapes
    "idsapext" -> IDSAPEXT.shapes
    "idsbase" -> IDSBASE.shapes
    else -> IDSBASE.shapes
}
