package forms

import forms.behaviours.StringFieldBehaviours
import play.api.data.FormError

class ReportingCompanyCTUTRFormProviderSpec extends StringFieldBehaviours {

  val requiredKey = "reportingCompanyCTUTR.error.required"
  val lengthKey = "reportingCompanyCTUTR.error.length"
  val maxLength = 10

  val form = new ReportingCompanyCTUTRFormProvider()()

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
