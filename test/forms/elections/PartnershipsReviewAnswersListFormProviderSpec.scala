package forms.elections

import forms.behaviours.BooleanFieldBehaviours
import play.api.data.FormError

class PartnershipsReviewAnswersListFormProviderSpec extends BooleanFieldBehaviours {

  val requiredKey = "partnershipsReviewAnswersList.error.required"
  val invalidKey = "error.boolean"

  val form = new PartnershipsReviewAnswersListFormProvider()()

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
