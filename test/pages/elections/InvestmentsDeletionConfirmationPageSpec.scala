package pages.elections

import pages.behaviours.PageBehaviours

class InvestmentsDeletionConfirmationPageSpec extends PageBehaviours {

  "InvestmentsDeletionConfirmationPage" must {

    beRetrievable[Boolean](InvestmentsDeletionConfirmationPage)

    beSettable[Boolean](InvestmentsDeletionConfirmationPage)

    beRemovable[Boolean](InvestmentsDeletionConfirmationPage)
  }
}
