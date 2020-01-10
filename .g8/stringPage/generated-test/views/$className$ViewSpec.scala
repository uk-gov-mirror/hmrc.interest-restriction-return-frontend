package views

import controllers.routes
import forms.$className$FormProvider
import models.NormalMode
import play.api.data.Form
import play.api.libs.json.Json
import play.twirl.api.HtmlFormat
import uk.gov.hmrc.nunjucks.NunjucksSupport
import uk.gov.hmrc.viewmodels.Radios
import views.behaviours.StringViewBehaviours
import views.html.$className$View
import nunjucks.$className$Template
import nunjucks.viewmodels.BasicFormViewModel

class $className$ViewSpec extends StringViewBehaviours with NunjucksSupport {

  val messageKeyPrefix = "$className;format="decap"$"

  val form = new $className$FormProvider()()

  Seq(Nunjucks, Twirl).foreach { templatingSystem =>

    s"$className $ (\$templatingSystem) view" must {

      def applyView(form: Form[_]): HtmlFormat.Appendable =
        if (templatingSystem == Nunjucks) {
          await(nunjucksRenderer.render($className$Template, Json.toJsObject(BasicFormViewModel(form, NormalMode)))(fakeRequest))
        } else {
          val view = viewFor[$className$View](Some(emptyUserAnswers))
          view.apply(form, NormalMode)(fakeRequest, messages, frontendAppConfig)
        }

      behave like normalPage(applyView(form), messageKeyPrefix)

      behave like pageWithBackLink(applyView(form))

      behave like stringPage(form, applyView, messageKeyPrefix, routes.$className$Controller.onSubmit(NormalMode).url)
    }
  }
}
