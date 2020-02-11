package forms.$section;format="decap"$

import javax.inject.Inject

import forms.mappings.Mappings
import play.api.data.Form

class $className;format="cap"$FormProvider @Inject() extends Mappings {

  def apply(): Form[String] =
    Form(
      "value" -> text("$className;format="decap"$.error.required")
        .verifying(maxLength($maxLength$, "$className;format="decap"$.error.length"))
    )
}
