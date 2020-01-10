package forms

import forms.behaviours.StringFieldBehaviours
import play.api.data.FormError

class ReportingCompanyNameFormProviderSpec extends StringFieldBehaviours {

  val requiredKey = "reportingCompanyName.error.required"
  val lengthKey = "reportingCompanyName.error.length"
  val maxLength = 160

  val form = new ReportingCompanyNameFormProvider()()

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
