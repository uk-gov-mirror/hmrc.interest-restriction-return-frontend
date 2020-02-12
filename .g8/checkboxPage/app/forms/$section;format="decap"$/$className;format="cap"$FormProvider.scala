package forms.$section;format="decap"$

import javax.inject.Inject

import forms.mappings.Mappings
import play.api.data.Form
import play.api.data.Forms.set
import models.$className;format="cap"$

class $className;format="cap"$FormProvider @Inject() extends Mappings {

  def apply(): Form[Set[$className;format="cap"$]] =
    Form(
      "value" -> set(enumerable[$className;format="cap"$]("$className;format="decap"$.error.required")).verifying(nonEmptySet("$className;format="decap"$.error.required"))
    )
}
