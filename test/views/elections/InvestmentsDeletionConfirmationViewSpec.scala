package views.elections

import assets.messages.{BaseMessages, SectionHeaderMessages}
import controllers.elections.routes
import forms.elections.InvestmentsDeletionConfirmationFormProvider
import models.NormalMode
import play.api.data.Form
import play.twirl.api.HtmlFormat
import views.behaviours.YesNoViewBehaviours
import views.html.elections.InvestmentsDeletionConfirmationView

class InvestmentsDeletionConfirmationViewSpec extends YesNoViewBehaviours  {

  val messageKeyPrefix = "investmentsDeletionConfirmation"
  val section = Some(messages("section.elections"))
  val form = new InvestmentsDeletionConfirmationFormProvider()()

    "InvestmentsDeletionConfirmationView" must {

      def applyView(form: Form[_]): HtmlFormat.Appendable = {
        val view = viewFor[InvestmentsDeletionConfirmationView](Some(emptyUserAnswers))
        view.apply(form, NormalMode)(fakeRequest, messages, frontendAppConfig)
      }

      behave like normalPage(applyView(form), messageKeyPrefix, section = section)

      behave like pageWithSubHeading(applyView(form), SectionHeaderMessages.elections)

      behave like pageWithBackLink(applyView(form))

      behave like yesNoPage(form, applyView, messageKeyPrefix, routes.InvestmentsDeletionConfirmationController.onSubmit(NormalMode).url, section = section)

      behave like pageWithSubmitButton(applyView(form), BaseMessages.saveAndContinue)

      behave like pageWithSaveForLater(applyView(form))
    }
  }
