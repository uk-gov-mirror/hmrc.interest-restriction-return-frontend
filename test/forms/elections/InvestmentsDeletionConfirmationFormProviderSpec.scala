package forms.elections

import forms.behaviours.BooleanFieldBehaviours
import play.api.data.FormError

class InvestmentsDeletionConfirmationFormProviderSpec extends BooleanFieldBehaviours {

  val requiredKey = "investmentsDeletionConfirmation.error.required"
  val invalidKey = "error.boolean"

  val form = new InvestmentsDeletionConfirmationFormProvider()()

  ".value" must {

    val fieldName = "value"

    behave like booleanField(
      form,
      fieldName,
      invalidError = FormError(fieldName, invalidKey)
    )

    behave like mandatoryField(
      form,
      fieldName,
      requiredError = FormError(fieldName, requiredKey)
    )
  }
}
