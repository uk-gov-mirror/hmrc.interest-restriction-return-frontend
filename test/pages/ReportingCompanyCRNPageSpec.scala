package pages

import pages.behaviours.PageBehaviours


class ReportingCompanyCRNPageSpec extends PageBehaviours {

  "ReportingCompanyCRNPage" must {

    beRetrievable[String](ReportingCompanyCRNPage)

    beSettable[String](ReportingCompanyCRNPage)

    beRemovable[String](ReportingCompanyCRNPage)
  }
}
