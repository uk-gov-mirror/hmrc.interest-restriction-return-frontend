package forms

import forms.behaviours.OptionFieldBehaviours
import models.$className;format="cap"$
import play.api.data.FormError

class $className;format="cap"$FormProviderSpec extends OptionFieldBehaviours {

  val form = new $className;format="cap"$FormProvider()()

  ".value" must {

    val fieldName = "value"
    val requiredKey = "$className;format="decap"$.error.required"

    behave like optionsField[$className;format="cap"$](
      form,
      fieldName,
      validValues  = $className;format="cap"$.values,
      invalidError = FormError(fieldName, "error.invalid")
    )

    behave like mandatoryField(
      form,
      fieldName,
      requiredError = FormError(fieldName, requiredKey)
    )
  }
}
