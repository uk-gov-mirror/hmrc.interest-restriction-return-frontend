package views

import forms.$className$FormProvider
import models.{$className$, ContinueSavedReturn, NormalMode}
import play.api.data.Form
import play.api.libs.json.Json
import play.twirl.api.HtmlFormat
import uk.gov.hmrc.viewmodels.Radios
import views.behaviours.ViewBehaviours
import views.html.$className$View

class $className$ViewSpec extends ViewBehaviours  {

  val messageKeyPrefix = "$className;format="decap"$"

  val form = new $className$FormProvider()()

  Seq(Twirl).foreach { templatingSystem =>

    s"$className $ (\$templatingSystem) view" must {

      def applyView(form: Form[$className$]): HtmlFormat.Appendable =
        if (templatingSystem == ) {
          await(Renderer.render($className$Template, Json.toJsObject(RadioOptionsViewModel(
            $className$.options(form),
            form,
            NormalMode
          )))(fakeRequest))
        } else {
          val view = viewFor[$className$View](Some(emptyUserAnswers))
          view.apply(form, NormalMode)(fakeRequest, messages, frontendAppConfig)
        }

      behave like normalPage(applyView(form), messageKeyPrefix)

      behave like pageWithBackLink(applyView(form))

      $className$.options(form).zipWithIndex.foreach { case (option, i) =>

        val id = if(i == 0) "value" else s"value-\${i + 1}"

        s"contain radio buttons for the value '\${option.value.get}'" in {

          val doc = asDocument(applyView(form))
          assertContainsRadioButton(doc, id, "value", option.value.get, false)
        }

        s"rendered with a value of '\${option.value.get}'" must {

          s"have the '\${option.value.get}' radio button selected" in {

            val formWithData = form.bind(Map("value" -> s"\${option.value.get}"))
            val doc = asDocument(applyView(formWithData))

            assertContainsRadioButton(doc, id, "value", option.value.get, true)
          }
        }
      }
    }
  }
}
