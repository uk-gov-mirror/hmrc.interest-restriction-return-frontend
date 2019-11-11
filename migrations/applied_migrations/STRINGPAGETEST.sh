#!/bin/bash

echo ""
echo "Applying migration STRINGPAGETEST"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /sTRINGPAGETEST                        controllers.STRINGPAGETESTController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /sTRINGPAGETEST                        controllers.STRINGPAGETESTController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeSTRINGPAGETEST                  controllers.STRINGPAGETESTController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeSTRINGPAGETEST                  controllers.STRINGPAGETESTController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "sTRINGPAGETEST.title = sTRINGPAGETEST" >> ../conf/messages.en
echo "sTRINGPAGETEST.heading = sTRINGPAGETEST" >> ../conf/messages.en
echo "sTRINGPAGETEST.checkYourAnswersLabel = sTRINGPAGETEST" >> ../conf/messages.en
echo "sTRINGPAGETEST.label = MyNewPage" >> ../conf/messages.en
echo "sTRINGPAGETEST.error.required = Enter sTRINGPAGETEST" >> ../conf/messages.en
echo "sTRINGPAGETEST.error.length = STRINGPAGETEST must be 100 characters or less" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitrarySTRINGPAGETESTUserAnswersEntry: Arbitrary[(STRINGPAGETESTPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[STRINGPAGETESTPage.type]";\
    print "        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitrarySTRINGPAGETESTPage: Arbitrary[STRINGPAGETESTPage.type] =";\
    print "    Arbitrary(STRINGPAGETESTPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(STRINGPAGETESTPage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def sTRINGPAGETEST: Option[SummaryListRow] = inputText(STRINGPAGETESTPage, routes.STRINGPAGETESTController.onPageLoad(CheckMode))";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Migration STRINGPAGETEST completed"
