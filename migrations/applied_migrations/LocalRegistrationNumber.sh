#!/bin/bash

echo ""
echo "Applying migration LocalRegistrationNumber"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "### LocalRegistrationNumber Controller" >> ../conf/app.routes
echo "### ----------------------------------------" >> ../conf/app.routes

export kebabClassName=$(sed -e 's/\([^A-Z]\)\([A-Z0-9]\)/\1-\2/g' -e 's/\([A-Z0-9]\)\([A-Z0-9]\)\([^A-Z]\)/\1-\2\3/g' <<< "LocalRegistrationNumber" | tr '[:upper:]' '[:lower:]')
echo "GET        /$kebabClassName                          controllers.ultimateParentCompany.LocalRegistrationNumberController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /$kebabClassName                          controllers.ultimateParentCompany.LocalRegistrationNumberController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "GET        /$kebabClassName/change                   controllers.ultimateParentCompany.LocalRegistrationNumberController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /$kebabClassName/change                   controllers.ultimateParentCompany.LocalRegistrationNumberController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to English conf.messages"
echo "" >> ../conf/messages.en
echo "# LocalRegistrationNumberPage Messages" >> ../conf/messages.en
echo "# ----------------------------------------------------------" >> ../conf/messages.en
echo "localRegistrationNumber.title = localRegistrationNumber" >> ../conf/messages.en
echo "localRegistrationNumber.heading = localRegistrationNumber" >> ../conf/messages.en
echo "localRegistrationNumber.checkYourAnswersLabel = localRegistrationNumber" >> ../conf/messages.en
echo "localRegistrationNumber.label= LocalRegistrationNumber" >> ../conf/messages.en
echo "localRegistrationNumber.error.required = Enter localRegistrationNumber" >> ../conf/messages.en
echo "localRegistrationNumber.error.length = LocalRegistrationNumber must be 100 characters or less" >> ../conf/messages.en

echo "Adding messages to Welsh conf.messages"
echo "" >> ../conf/messages.cy
echo "# LocalRegistrationNumberPage Messages" >> ../conf/messages.cy
echo "# ----------------------------------------------------------" >> ../conf/messages.cy
echo "localRegistrationNumber.title = localRegistrationNumber" >> ../conf/messages.cy
echo "localRegistrationNumber.heading = localRegistrationNumber" >> ../conf/messages.cy
echo "localRegistrationNumber.checkYourAnswersLabel = localRegistrationNumber" >> ../conf/messages.cy
echo "localRegistrationNumber.label = LocalRegistrationNumber" >> ../conf/messages.cy
echo "localRegistrationNumber.error.required = Enter localRegistrationNumber" >> ../conf/messages.cy
echo "localRegistrationNumber.error.length = LocalRegistrationNumber must be 100 characters or less" >> ../conf/messages.cy

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryLocalRegistrationNumberUserAnswersEntry: Arbitrary[(LocalRegistrationNumberPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[LocalRegistrationNumberPage.type]";\
    print "        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryLocalRegistrationNumberPage: Arbitrary[LocalRegistrationNumberPage.type] =";\
    print "    Arbitrary(LocalRegistrationNumberPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(LocalRegistrationNumberPage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def localRegistrationNumber: Option[SummaryListRow] = answer(LocalRegistrationNumberPage, routes.LocalRegistrationNumberController.onPageLoad(CheckMode))";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Adding to Pages map"
awk '/val pages/ {\
    print;\
    print "    LocalRegistrationNumberPage.toString -> LocalRegistrationNumberPage,";\
    next }1' ../app/pages/Page.scala > tmp && mv tmp ../app/pages/Page.scala

echo "Migration LocalRegistrationNumber completed"
