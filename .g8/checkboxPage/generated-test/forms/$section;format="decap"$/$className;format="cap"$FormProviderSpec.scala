package forms.$section;format="decap"$

import forms.behaviours.CheckboxFieldBehaviours
import models.$className;format="cap"$
import play.api.data.FormError

class $className;format="cap"$FormProviderSpec extends CheckboxFieldBehaviours {

  val form = new $className;format="cap"$FormProvider()()

  ".value" must {

    val fieldName = "value"
    val requiredKey = "$className;format="decap"$.error.required"

    behave like checkboxField[$className;format="cap"$](
      form,
      fieldName,
      validValues  = $className;format="cap"$.values,
      invalidError = FormError(s"\$fieldName[0]", "error.invalid")
    )

    behave like mandatoryCheckboxField(
      form,
      fieldName,
      requiredKey
    )
  }
}
