package forms.$section;format="decap"$

import forms.behaviours.BooleanFieldBehaviours
import play.api.data.FormError

class $className;format="cap"$FormProviderSpec extends BooleanFieldBehaviours {

  val requiredKey = "$className;format="decap"$.error.required"
  val invalidKey = "error.boolean"

  val form = new $className;format="cap"$FormProvider()()

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
