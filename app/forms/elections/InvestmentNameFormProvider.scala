package forms.elections

import javax.inject.Inject

import forms.mappings.Mappings
import play.api.data.Form

class InvestmentNameFormProvider @Inject() extends Mappings {

  def apply(): Form[String] =
    Form(
      "value" -> text("investmentName.error.required")
        .verifying(maxLength(160, "investmentName.error.length"))
    )
}
