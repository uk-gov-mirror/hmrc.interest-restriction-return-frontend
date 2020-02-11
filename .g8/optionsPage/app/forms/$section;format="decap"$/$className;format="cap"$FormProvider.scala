package forms

import javax.inject.Inject

import forms.mappings.Mappings
import play.api.data.Form
import models.$className;format="cap"$

class $className;format="cap"$FormProvider @Inject() extends Mappings {

  def apply(): Form[$className;format="cap"$] =
    Form(
      "value" -> enumerable[$className;format="cap"$]("$className;format="decap"$.error.required")
    )
}
