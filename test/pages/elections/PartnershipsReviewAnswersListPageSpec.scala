package pages.elections

import pages.behaviours.PageBehaviours

class PartnershipsReviewAnswersListPageSpec extends PageBehaviours {

  "PartnershipsReviewAnswersListPage" must {

    beRetrievable[Boolean](PartnershipsReviewAnswersListPage)

    beSettable[Boolean](PartnershipsReviewAnswersListPage)

    beRemovable[Boolean](PartnershipsReviewAnswersListPage)
  }
}
