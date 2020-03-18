#!/bin/bash

echo ""
echo "Applying migration ParentCompanyCTUTR"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /parentCompanyCTUTR                        controllers.ultimateParentCompany.ParentCompanyCTUTRController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /parentCompanyCTUTR                        controllers.ultimateParentCompany.ParentCompanyCTUTRController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeParentCompanyCTUTR                  controllers.ultimateParentCompany.ParentCompanyCTUTRController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeParentCompanyCTUTR                  controllers.ultimateParentCompany.ParentCompanyCTUTRController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to English conf.messages"
echo "" >> ../conf/messages.en
echo "" >> ../conf/messages.en
echo "# ParentCompanyCTUTRPage Messages" >> ../conf/messages.en
echo "# ----------------------------------------------------------" >> ../conf/messages.en
echo "parentCompanyCTUTR.title = parentCompanyCTUTR" >> ../conf/messages.en
echo "parentCompanyCTUTR.heading = parentCompanyCTUTR" >> ../conf/messages.en
echo "parentCompanyCTUTR.checkYourAnswersLabel = parentCompanyCTUTR" >> ../conf/messages.en
echo "parentCompanyCTUTR.label = Parent Company Corporation Unique Tax Payer Reference" >> ../conf/messages.en
echo "parentCompanyCTUTR.error.required = Enter parentCompanyCTUTR" >> ../conf/messages.en
echo "parentCompanyCTUTR.error.length = ParentCompanyCTUTR must be 10 characters or less" >> ../conf/messages.en

echo "Adding messages to Welsh conf.messages"
echo "" >> ../conf/messages.cy
echo "" >> ../conf/messages.cy
echo "# ParentCompanyCTUTRPage Messages" >> ../conf/messages.cy
echo "# ----------------------------------------------------------" >> ../conf/messages.cy
echo "parentCompanyCTUTR.title = parentCompanyCTUTR" >> ../conf/messages.cy
echo "parentCompanyCTUTR.heading = parentCompanyCTUTR" >> ../conf/messages.cy
echo "parentCompanyCTUTR.checkYourAnswersLabel = parentCompanyCTUTR" >> ../conf/messages.cy
echo "parentCompanyCTUTR.label = Parent Company Corporation Unique Tax Payer Reference" >> ../conf/messages.cy
echo "parentCompanyCTUTR.error.required = Enter parentCompanyCTUTR" >> ../conf/messages.cy
echo "parentCompanyCTUTR.error.length = ParentCompanyCTUTR must be 10 characters or less" >> ../conf/messages.cy

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryParentCompanyCTUTRUserAnswersEntry: Arbitrary[(ParentCompanyCTUTRPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[ParentCompanyCTUTRPage.type]";\
    print "        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryParentCompanyCTUTRPage: Arbitrary[ParentCompanyCTUTRPage.type] =";\
    print "    Arbitrary(ParentCompanyCTUTRPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(ParentCompanyCTUTRPage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def parentCompanyCTUTR: Option[SummaryListRow] = answer(ParentCompanyCTUTRPage, routes.ParentCompanyCTUTRController.onPageLoad(CheckMode))";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Adding to Pages map"
awk '/val pages/ {\
    print;\
    print "    ParentCompanyCTUTRPage.toString -> ParentCompanyCTUTRPage,";\
    next }1' ../app/pages/Page.scala > tmp && mv tmp ../app/pages/Page.scala

echo "Migration ParentCompanyCTUTR completed"
