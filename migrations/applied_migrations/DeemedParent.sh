#!/bin/bash

echo ""
echo "Applying migration DeemedParent"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /deemedParent                        controllers.ultimateParentCompany.DeemedParentController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /deemedParent                        controllers.ultimateParentCompany.DeemedParentController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeDeemedParent                  controllers.ultimateParentCompany.DeemedParentController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeDeemedParent                  controllers.ultimateParentCompany.DeemedParentController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to English conf.messages"
echo "" >> ../conf/messages.en
echo "" >> ../conf/messages.en
echo "# DeemedParentPage Messages" >> ../conf/messages.en
echo "# ----------------------------------------------------------" >> ../conf/messages.en
echo "deemedParent.title = Does the group have a 'deemed' parent company?" >> ../conf/messages.en
echo "deemedParent.heading = Does the group have a 'deemed' parent company?" >> ../conf/messages.en
echo "deemedParent.checkYourAnswersLabel = Does the group have a 'deemed' parent company?" >> ../conf/messages.en
echo "deemedParent.error.required = Select yes if Does the group have a 'deemed' parent company?" >> ../conf/messages.en

echo "Adding messages to Welsh conf.messages"
echo "" >> ../conf/messages.cy
echo "" >> ../conf/messages.cy
echo "# DeemedParentPage Messages" >> ../conf/messages.cy
echo "# ----------------------------------------------------------" >> ../conf/messages.cy
echo "deemedParent.title = Does the group have a 'deemed' parent company?" >> ../conf/messages.cy
echo "deemedParent.heading = Does the group have a 'deemed' parent company?" >> ../conf/messages.cy
echo "deemedParent.checkYourAnswersLabel = Does the group have a 'deemed' parent company?" >> ../conf/messages.cy
echo "deemedParent.error.required = Select yes if Does the group have a 'deemed' parent company?" >> ../conf/messages.cy

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryDeemedParentUserAnswersEntry: Arbitrary[(DeemedParentPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[DeemedParentPage.type]";\
    print "        value <- arbitrary[Boolean].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryDeemedParentPage: Arbitrary[DeemedParentPage.type] =";\
    print "    Arbitrary(DeemedParentPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(DeemedParentPage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def deemedParent: Option[SummaryListRow] = answer(DeemedParentPage, routes.DeemedParentController.onPageLoad(CheckMode))";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Adding to Pages map"
awk '/val pages/ {\
    print;\
    print "    DeemedParentPage.toString -> DeemedParentPage,";\
    next }1' ../app/pages/Page.scala > tmp && mv tmp ../app/pages/Page.scala

echo "Migration DeemedParent completed"
