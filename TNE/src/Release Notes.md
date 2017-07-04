Alpha 5.6
=============================
- Recoded TNE for stability reasons
  - TNE now uses a module-based system to give server owners even more control
  - This new system allows third-party developers to extend TNE's functionality easier, and without going through the hassle of creating a new plugin entirely
  - The new system also allows issues in individual modules to be fixed without updating the main TNE plugin itself
- Configurations
  - Reordered some of the currency configurations
  - Changed some of the default configurations based on user feedback.
- Currency
  - Added ability for server owners to change the weight of the minor currency
  - Added ability to separate the major value every three numeric places 
    - The character used to separate is configurable per currency.
- Accounts
  - We now track when the account owner was last online
- Bug Fixes
  - Fixed conversion issues
  - Fixed balance formatting issues players were having when multiworld was disabled