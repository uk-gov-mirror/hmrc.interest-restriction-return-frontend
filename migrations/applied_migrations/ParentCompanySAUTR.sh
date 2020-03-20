#!/bin/bash

echo ""
echo "Applying migration ParentCompanySAUTR"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /parentCompanySAUTR                  controllers.ultimateParentCompany.ParentCompanySAUTRController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /parentCompanySAUTR                  controllers.ultimateParentCompany.ParentCompanySAUTRController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeParentCompanySAUTR                        controllers.ultimateParentCompany.ParentCompanySAUTRController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeParentCompanySAUTR                        controllers.ultimateParentCompany.ParentCompanySAUTRController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to English conf.messages"
echo "" >> ../conf/messages.en
echo "" >> ../conf/messages.en
echo "# ParentCompanySAUTRPage Messages" >> ../conf/messages.en
echo "# ----------------------------------------------------------" >> ../conf/messages.en
echo "parentCompanySAUTR.title = ParentCompanySAUTR" >> ../conf/messages.en
echo "parentCompanySAUTR.heading = ParentCompanySAUTR" >> ../conf/messages.en
echo "parentCompanySAUTR.label = Parent company Self Assesment Unique Tax Payer Reference" >> ../conf/messages.en
echo "parentCompanySAUTR.checkYourAnswersLabel = ParentCompanySAUTR" >> ../conf/messages.en
echo "parentCompanySAUTR.error.nonNumeric = Enter your parentCompanySAUTR using numbers" >> ../conf/messages.en
echo "parentCompanySAUTR.error.required = Enter your parentCompanySAUTR" >> ../conf/messages.en
echo "parentCompanySAUTR.error.wholeNumber = Enter your parentCompanySAUTR using whole numbers" >> ../conf/messages.en
echo "parentCompanySAUTR.error.outOfRange = ParentCompanySAUTR must be between {0} and {1}" >> ../conf/messages.en

echo "Adding messages to Welsh conf.messages"
echo "" >> ../conf/messages.cy
echo "" >> ../conf/messages.cy
echo "# ParentCompanySAUTRPage Messages" >> ../conf/messages.cy
echo "# ----------------------------------------------------------" >> ../conf/messages.cy
echo "parentCompanySAUTR.title = ParentCompanySAUTR" >> ../conf/messages.cy
echo "parentCompanySAUTR.heading = ParentCompanySAUTR" >> ../conf/messages.cy
echo "parentCompanySAUTR.label =Parent company Self Assesment Unique Tax Payer Reference" >> ../conf/messages.cy
echo "parentCompanySAUTR.checkYourAnswersLabel = ParentCompanySAUTR" >> ../conf/messages.cy
echo "parentCompanySAUTR.error.nonNumeric = Enter your parentCompanySAUTR using numbers" >> ../conf/messages.cy
echo "parentCompanySAUTR.error.required = Enter your parentCompanySAUTR" >> ../conf/messages.cy
echo "parentCompanySAUTR.error.wholeNumber = Enter your parentCompanySAUTR using whole numbers" >> ../conf/messages.cy
echo "parentCompanySAUTR.error.outOfRange = ParentCompanySAUTR must be between {0} and {1}" >> ../conf/messages.cy

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryParentCompanySAUTRUserAnswersEntry: Arbitrary[(ParentCompanySAUTRPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[ParentCompanySAUTRPage.type]";\
    print "        value <- arbitrary[Int].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryParentCompanySAUTRPage: Arbitrary[ParentCompanySAUTRPage.type] =";\
    print "    Arbitrary(ParentCompanySAUTRPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(ParentCompanySAUTRPage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def parentCompanySAUTR: Option[SummaryListRow] = answer(ParentCompanySAUTRPage, routes.ParentCompanySAUTRController.onPageLoad(CheckMode))";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Adding to Pages map"
awk '/val pages/ {\
    print;\
    print "    ParentCompanySAUTRPage.toString -> ParentCompanySAUTRPage,";\
    next }1' ../app/pages/Page.scala > tmp && mv tmp ../app/pages/Page.scala

echo "Migration ParentCompanySAUTR completed"
