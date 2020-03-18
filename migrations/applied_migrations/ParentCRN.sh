#!/bin/bash

echo ""
echo "Applying migration ParentCRN"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /parentCRN                        controllers.ultimateParentCompany.ParentCRNController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /parentCRN                        controllers.ultimateParentCompany.ParentCRNController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeParentCRN                  controllers.ultimateParentCompany.ParentCRNController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeParentCRN                  controllers.ultimateParentCompany.ParentCRNController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to English conf.messages"
echo "" >> ../conf/messages.en
echo "" >> ../conf/messages.en
echo "# ParentCRNPage Messages" >> ../conf/messages.en
echo "# ----------------------------------------------------------" >> ../conf/messages.en
echo "parentCRN.title = parentCRN" >> ../conf/messages.en
echo "parentCRN.heading = parentCRN" >> ../conf/messages.en
echo "parentCRN.checkYourAnswersLabel = parentCRN" >> ../conf/messages.en
echo "parentCRN.label = Parent Company Registration Number" >> ../conf/messages.en
echo "parentCRN.error.required = Enter parentCRN" >> ../conf/messages.en
echo "parentCRN.error.length = ParentCRN must be 8 characters or less" >> ../conf/messages.en

echo "Adding messages to Welsh conf.messages"
echo "" >> ../conf/messages.cy
echo "" >> ../conf/messages.cy
echo "# ParentCRNPage Messages" >> ../conf/messages.cy
echo "# ----------------------------------------------------------" >> ../conf/messages.cy
echo "parentCRN.title = parentCRN" >> ../conf/messages.cy
echo "parentCRN.heading = parentCRN" >> ../conf/messages.cy
echo "parentCRN.checkYourAnswersLabel = parentCRN" >> ../conf/messages.cy
echo "parentCRN.label = Parent Company Registration Number" >> ../conf/messages.cy
echo "parentCRN.error.required = Enter parentCRN" >> ../conf/messages.cy
echo "parentCRN.error.length = ParentCRN must be 8 characters or less" >> ../conf/messages.cy

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryParentCRNUserAnswersEntry: Arbitrary[(ParentCRNPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[ParentCRNPage.type]";\
    print "        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryParentCRNPage: Arbitrary[ParentCRNPage.type] =";\
    print "    Arbitrary(ParentCRNPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(ParentCRNPage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def parentCRN: Option[SummaryListRow] = answer(ParentCRNPage, routes.ParentCRNController.onPageLoad(CheckMode))";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Adding to Pages map"
awk '/val pages/ {\
    print;\
    print "    ParentCRNPage.toString -> ParentCRNPage,";\
    next }1' ../app/pages/Page.scala > tmp && mv tmp ../app/pages/Page.scala

echo "Migration ParentCRN completed"
