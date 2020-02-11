package forms.$section;format="decap"$

import forms.mappings.Mappings
import javax.inject.Inject
import play.api.data.Form

class $className;format="cap"$FormProvider @Inject() extends Mappings {

  def apply(): Form[BigDecimal] =
    Form(
      "value" -> numeric(
        "$className;format="decap"$.error.required",
        "$className;format="decap"$.error.invalidNumeric",
        "$className;format="decap"$.error.nonNumeric")
        .verifying(inRange[BigDecimal]($minimum$, $maximum$, "$className;format="decap"$.error.outOfRange"))
    )
}
