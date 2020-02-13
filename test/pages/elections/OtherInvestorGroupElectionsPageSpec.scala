package pages.elections

import models.OtherInvestorGroupElections
import pages.behaviours.PageBehaviours

class OtherInvestorGroupElectionsPageSpec extends PageBehaviours {

  "OtherInvestorGroupElectionsPage" must {

    beRetrievable[Set[OtherInvestorGroupElections]](OtherInvestorGroupElectionsPage)

    beSettable[Set[OtherInvestorGroupElections]](OtherInvestorGroupElectionsPage)

    beRemovable[Set[OtherInvestorGroupElections]](OtherInvestorGroupElectionsPage)
  }
}
