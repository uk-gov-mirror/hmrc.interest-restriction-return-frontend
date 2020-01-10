package pages

import pages.behaviours.PageBehaviours


class ReportingCompanyCTUTRPageSpec extends PageBehaviours {

  "ReportingCompanyCTUTRPage" must {

    beRetrievable[String](ReportingCompanyCTUTRPage)

    beSettable[String](ReportingCompanyCTUTRPage)

    beRemovable[String](ReportingCompanyCTUTRPage)
  }
}
