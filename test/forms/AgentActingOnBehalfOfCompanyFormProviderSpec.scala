package forms

import forms.behaviours.BooleanFieldBehaviours
import play.api.data.FormError

class AgentActingOnBehalfOfCompanyFormProviderSpec extends BooleanFieldBehaviours {

  val requiredKey = "agentActingOnBehalfOfCompany.error.required"
  val invalidKey = "error.boolean"

  val form = new AgentActingOnBehalfOfCompanyFormProvider()()

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
