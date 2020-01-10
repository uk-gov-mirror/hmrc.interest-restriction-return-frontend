package views

import forms.$className$FormProvider
import models.{$className$, NormalMode}
import play.api.data.Form
import play.api.libs.json.Json
import play.twirl.api.HtmlFormat
import uk.gov.hmrc.nunjucks.NunjucksSupport
import uk.gov.hmrc.viewmodels.Radios
import views.behaviours.ViewBehaviours
import views.html.$className$View
import nunjucks.$className$Template
import nunjucks.viewmodels.RadioOptionsViewModel

class $className$ViewSpec extends ViewBehaviours with NunjucksSupport {

  val messageKeyPrefix = "$className;format="decap"$"

  val form = new $className$FormProvider()()

  Seq(Nunjucks, Twirl).foreach { templatingSystem =>

    s"$className $ (\$templatingSystem) view" must {

      def applyView(form: Form[$className$]): HtmlFormat.Appendable =
        if (templatingSystem == Nunjucks) {
          await(nunjucksRenderer.render($className$Template, Json.toJsObject(RadioOptionsViewModel(
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

      "rendered" must {

        "contain radio buttons for the value" in {

          val doc = asDocument(applyView(form))

          for (option <- $className$.options(form)) {
            assertContainsRadioButton(doc, option.id.get, "value", option.value.get, false)
          }
        }
      }

      for (option <- $className$.options(form)) {

        s"rendered with a value of '\${option.value.get}'" must {

          s"have the '\${option.value.get}' radio button selected" in {

            val formWithData = form.bind(Map("value" -> s"\${option.value.get}"))
            val doc = asDocument(applyView(formWithData))

            assertContainsRadioButton(doc, option.id.get, "value", option.value.get, true)
          }
        }
      }
    }
  }
}
