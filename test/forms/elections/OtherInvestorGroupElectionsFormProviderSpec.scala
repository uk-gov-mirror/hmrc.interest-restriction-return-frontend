package forms.elections

import forms.behaviours.CheckboxFieldBehaviours
import models.OtherInvestorGroupElections
import play.api.data.FormError

class OtherInvestorGroupElectionsFormProviderSpec extends CheckboxFieldBehaviours {

  val form = new OtherInvestorGroupElectionsFormProvider()()

  ".value" must {

    val fieldName = "value"
    val requiredKey = "otherInvestorGroupElections.error.required"

    behave like checkboxField[OtherInvestorGroupElections](
      form,
      fieldName,
      validValues  = OtherInvestorGroupElections.values,
      invalidError = FormError(s"$fieldName[0]", "error.invalid")
    )

    behave like mandatoryCheckboxField(
      form,
      fieldName,
      requiredKey
    )
  }
}
