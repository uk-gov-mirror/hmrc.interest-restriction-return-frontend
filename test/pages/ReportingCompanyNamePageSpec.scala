package pages

import pages.behaviours.PageBehaviours


class ReportingCompanyNamePageSpec extends PageBehaviours {

  "ReportingCompanyNamePage" must {

    beRetrievable[String](ReportingCompanyNamePage)

    beSettable[String](ReportingCompanyNamePage)

    beRemovable[String](ReportingCompanyNamePage)
  }
}
