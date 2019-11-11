#!/bin/bash

echo ""
echo "Applying migration TESTIntPageTEST"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /tESTIntPageTEST                  controllers.TESTIntPageTESTController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /tESTIntPageTEST                  controllers.TESTIntPageTESTController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeTESTIntPageTEST                        controllers.TESTIntPageTESTController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeTESTIntPageTEST                        controllers.TESTIntPageTESTController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "tESTIntPageTEST.title = TESTIntPageTEST" >> ../conf/messages.en
echo "tESTIntPageTEST.heading = TESTIntPageTEST" >> ../conf/messages.en
echo "tESTIntPageTEST.checkYourAnswersLabel = TESTIntPageTEST" >> ../conf/messages.en
echo "tESTIntPageTEST.error.nonNumeric = Enter your tESTIntPageTEST using numbers" >> ../conf/messages.en
echo "tESTIntPageTEST.error.required = Enter your tESTIntPageTEST" >> ../conf/messages.en
echo "tESTIntPageTEST.error.wholeNumber = Enter your tESTIntPageTEST using whole numbers" >> ../conf/messages.en
echo "tESTIntPageTEST.error.outOfRange = TESTIntPageTEST must be between {0} and {1}" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryTESTIntPageTESTUserAnswersEntry: Arbitrary[(TESTIntPageTESTPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[TESTIntPageTESTPage.type]";\
    print "        value <- arbitrary[Int].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryTESTIntPageTESTPage: Arbitrary[TESTIntPageTESTPage.type] =";\
    print "    Arbitrary(TESTIntPageTESTPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(TESTIntPageTESTPage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def tESTIntPageTEST: Option[AnswerRow] = userAnswers.get(TESTIntPageTESTPage) map {";\
     print "    x =>";\
     print "      AnswerRow(";\
     print "        HtmlFormat.escape(messages(\"tESTIntPageTEST.checkYourAnswersLabel\")),";\
     print "        HtmlFormat.escape(x.toString),";\
     print "        routes.TESTIntPageTESTController.onPageLoad(CheckMode).url";\
     print "      )"
     print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Migration TESTIntPageTEST completed"
