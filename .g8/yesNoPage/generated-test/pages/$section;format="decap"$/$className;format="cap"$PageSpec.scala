package pages.$section;format="decap"$

import pages.behaviours.PageBehaviours

class $className;format="cap"$PageSpec extends PageBehaviours {

  "$className;format="cap"$Page" must {

    beRetrievable[Boolean]($className;format="cap"$Page)

    beSettable[Boolean]($className;format="cap"$Page)

    beRemovable[Boolean]($className;format="cap"$Page)
  }
}
