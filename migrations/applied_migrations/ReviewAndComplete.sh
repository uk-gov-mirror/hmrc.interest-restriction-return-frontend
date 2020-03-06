#!/bin/bash

echo ""
echo "Applying migration ReviewAndComplete"

echo "" >> ../conf/reviewAndComplete.routes
echo "### ReviewAndComplete Controller" >> ../conf/reviewAndComplete.routes
echo "### ----------------------------------------" >> ../conf/reviewAndComplete.routes
export kebabClassName=$(sed -e 's/\([^A-Z]\)\([A-Z0-9]\)/\1-\2/g' -e 's/\([A-Z0-9]\)\([A-Z0-9]\)\([^A-Z]\)/\1-\2\3/g' <<< "ReviewAndComplete" | tr '[:upper:]' '[:lower:]')
echo "GET        /$kebabClassName                         controllers.reviewAndComplete.ReviewAndCompleteController.onPageLoad()" >> ../conf/reviewAndComplete.routes

echo "Adding messages to English conf.messages"
echo "" >> ../conf/messages.en
echo "# ReviewAndCompletePage Messages" >> ../conf/messages.en
echo "# ----------------------------------------------------------" >> ../conf/messages.en
echo "reviewAndComplete.title = reviewAndComplete" >> ../conf/messages.en
echo "reviewAndComplete.heading = reviewAndComplete" >> ../conf/messages.en

echo "Adding messages to Welsh conf.messages"
echo "" >> ../conf/messages.cy
echo "# ReviewAndCompletePage Messages" >> ../conf/messages.cy
echo "# ----------------------------------------------------------" >> ../conf/messages.cy
echo "reviewAndComplete.title = reviewAndComplete" >> ../conf/messages.cy
echo "reviewAndComplete.heading = reviewAndComplete" >> ../conf/messages.cy

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryReviewAndCompletePage: Arbitrary[ReviewAndCompletePage.type] =";\
    print "    Arbitrary(ReviewAndCompletePage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "adding to PageTitles"
awk '/object PageTitles/ {\
    print;\
    print "  val reviewAndComplete = \"Interest Restriction Return\"";\
    next }1' ../it/assets/PageTitles.scala > tmp && mv tmp ../it/assets/PageTitles.scala

echo "Adding to Pages map"
awk '/val pages/ {\
    print;\
    print "    ReviewAndCompletePage.toString -> ReviewAndCompletePage,";\
    next }1' ../app/pages/Page.scala > tmp && mv tmp ../app/pages/Page.scala

echo "adding to Pages map spec"
awk '/val expected/ {\
    print;\
    print "    ReviewAndCompletePage.toString -> ReviewAndCompletePage,";\
    next }1' ../test/pages/PageSpec.scala > tmp && mv tmp ../test/pages/PageSpec.scala

echo "adding route to integration test"

sed -i "" "s|ROUTING_PLACEHOLDER|reviewAndComplete\/${kebabClassName}|g" ../generated-it/controllers/reviewAndComplete/ReviewAndCompleteControllerISpec.scala

echo "Migration ReviewAndComplete completed"
