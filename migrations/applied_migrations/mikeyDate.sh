#!/bin/bash

echo ""
echo "Applying migration mikeyDate"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /mikeyDate                  controllers.mikeyDateController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /mikeyDate                  controllers.mikeyDateController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changemikeyDate                        controllers.mikeyDateController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changemikeyDate                        controllers.mikeyDateController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to English conf.messages"
echo "" >> ../conf/messages.en
echo "" >> ../conf/messages.en
echo "# mikeyDatePage Messages" >> ../conf/messages.en
echo "# ----------------------------------------------------------" >> ../conf/messages.en
echo "mikeyDate.title = mikeyDate" >> ../conf/messages.en
echo "mikeyDate.heading = mikeyDate" >> ../conf/messages.en
echo "mikeyDate.checkYourAnswersLabel = mikeyDate" >> ../conf/messages.en
echo "mikeyDate.error.required.all = Enter the mikeyDate" >> ../conf/messages.en
echo "mikeyDate.error.required.two = The mikeyDate" must include {0} and {1} >> ../conf/messages.en
echo "mikeyDate.error.required = The mikeyDate must include {0}" >> ../conf/messages.en
echo "mikeyDate.error.invalid = Enter a real mikeyDate" >> ../conf/messages.en

echo "Adding messages to Welsh conf.messages"
echo "" >> ../conf/messages.cy
echo "" >> ../conf/messages.cy
echo "# mikeyDatePage Messages" >> ../conf/messages.cy
echo "# ----------------------------------------------------------" >> ../conf/messages.cy
echo "mikeyDate.title = mikeyDate" >> ../conf/messages.cy
echo "mikeyDate.heading = mikeyDate" >> ../conf/messages.cy
echo "mikeyDate.checkYourAnswersLabel = mikeyDate" >> ../conf/messages.cy
echo "mikeyDate.error.required.all = Enter the mikeyDate" >> ../conf/messages.cy
echo "mikeyDate.error.required.two = The mikeyDate" must include {0} and {1} >> ../conf/messages.cy
echo "mikeyDate.error.required = The mikeyDate must include {0}" >> ../conf/messages.cy
echo "mikeyDate.error.invalid = Enter a real mikeyDate" >> ../conf/messages.cy

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitrarymikeyDateUserAnswersEntry: Arbitrary[(mikeyDatePage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[mikeyDatePage.type]";\
    print "        value <- arbitrary[Int].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitrarymikeyDatePage: Arbitrary[mikeyDatePage.type] =";\
    print "    Arbitrary(mikeyDatePage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(mikeyDatePage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class CheckYourAnswersHelper/ {\
     print;\
     print "  def mikeyDate: Option[SummaryListRow] = answer(mikeyDatePage, routes.mikeyDateController.onPageLoad(CheckMode))";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Migration mikeyDate completed"
