package pages

import pages.behaviours.PageBehaviours

class ReportingCompanyAppointedPageSpec extends PageBehaviours {

  "ReportingCompanyAppointedPage" must {

    beRetrievable[Boolean](ReportingCompanyAppointedPage)

    beSettable[Boolean](ReportingCompanyAppointedPage)

    beRemovable[Boolean](ReportingCompanyAppointedPage)
  }
}
