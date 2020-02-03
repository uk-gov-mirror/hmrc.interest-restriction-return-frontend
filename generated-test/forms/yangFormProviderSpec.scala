package forms

import forms.behaviours.BooleanFieldBehaviours
import play.api.data.FormError

class yangFormProviderSpec extends BooleanFieldBehaviours {

  val requiredKey = "yang.error.required"
  val invalidKey = "error.boolean"

  val form = new yangFormProvider()()

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
