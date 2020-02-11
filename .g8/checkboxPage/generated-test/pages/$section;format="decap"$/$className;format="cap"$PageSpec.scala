package pages

import models.$className;format="cap"$
import pages.behaviours.PageBehaviours

class $className;format="cap"$PageSpec extends PageBehaviours {

  "$className;format="cap"$Page" must {

    beRetrievable[Set[$className;format="cap"$]]($className;format="cap"$Page)

    beSettable[Set[$className;format="cap"$]]($className;format="cap"$Page)

    beRemovable[Set[$className;format="cap"$]]($className;format="cap"$Page)
  }
}
