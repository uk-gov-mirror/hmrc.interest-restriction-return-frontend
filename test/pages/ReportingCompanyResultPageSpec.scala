package pages

import pages.behaviours.PageBehaviours

class ReportingCompanyResultPageSpec extends PageBehaviours {

  "ReportingCompanyResultPage" must {

    beRetrievable[Boolean](ReportingCompanyResultPage)

    beSettable[Boolean](ReportingCompanyResultPage)

    beRemovable[Boolean](ReportingCompanyResultPage)
  }
}
