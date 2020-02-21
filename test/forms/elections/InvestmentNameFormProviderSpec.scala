package forms.elections

import forms.behaviours.StringFieldBehaviours
import play.api.data.FormError

class InvestmentNameFormProviderSpec extends StringFieldBehaviours {

  val requiredKey = "investmentName.error.required"
  val lengthKey = "investmentName.error.length"
  val maxLength = 160

  val form = new InvestmentNameFormProvider()()

  ".value" must {

    val fieldName = "value"

    behave like fieldThatBindsValidData(
      form,
      fieldName,
      stringsWithMaxLength(maxLength)
    )

    behave like fieldWithMaxLength(
      form,
      fieldName,
      maxLength = maxLength,
      lengthError = FormError(fieldName, lengthKey, Seq(maxLength))
    )

    behave like mandatoryField(
      form,
      fieldName,
      requiredError = FormError(fieldName, requiredKey)
    )
  }
}
