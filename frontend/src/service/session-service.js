export class SessionService {
    BROWSER_STORAGE_ITEM_NAME;
    RESOURCE_URL;
    _currentToken;
    _currentUser;

    constructor(resourceUrl, browserStorageItemName) {
        console.log("Created SessionService...");
        this.BROWSER_STORAGE_ITEM_NAME = browserStorageItemName;
        this.RESOURCE_URL = resourceUrl;
        this._currentToken = null;
        this._currentUser = null;
        this.getTokenFromBrowserStorage();
    }

    get currentToken() {
        return this._currentToken;
    }

    get currentUser() {
        return this._currentUser;
    }

    isAdmin() {
        return this._currentUser?.role?.toLowerCase().includes("admin");
    }
    isAuthenticated() {
        return this._currentUser != null;
    }

    async asyncSignIn(email, password) {
        const body = JSON.stringify({
            email: email,
            password: password
        });
        let response = await fetch(this.RESOURCE_URL + '/login',
            {
                method: "POST",
                headers: { 'Content-Type': 'application/json'},
                body: body,
                credentials: "include"
            });
        if (response.ok) {
            let user = await response.json();
            this.saveTokenIntoBrowserStorage(response.headers.get('Authorization'), user);
            return user;
        } else {
            console.log(response);
            return null;
        }
    }

    saveTokenIntoBrowserStorage(token, user) {
        this._currentToken = token;
        this._currentUser = user;

        // allow for different user sessions from the same computer
        // sessionStorage keeps different items per browser tab
        // localStorage keeps a single item per browser vendor
        // both isolate the items per server domain of the page (including port number?)

        if (token == null) {
            this._currentUser = null;
            window.sessionStorage.removeItem(this.BROWSER_STORAGE_ITEM_NAME);
            window.sessionStorage.removeItem(this.BROWSER_STORAGE_ITEM_NAME+"_ACC");

            if (window.localStorage.getItem(this.BROWSER_STORAGE_ITEM_NAME) === window.sessionStorage.getItem(this.BROWSER_STORAGE_ITEM_NAME)) {
                window.localStorage.removeItem(this.BROWSER_STORAGE_ITEM_NAME);
                window.localStorage.removeItem(this.BROWSER_STORAGE_ITEM_NAME+"_ACC");
            }

        } else {
            console.log("New token for " + user.name + ": " + token);
            window.sessionStorage.setItem(this.BROWSER_STORAGE_ITEM_NAME, token);
            window.sessionStorage.setItem(this.BROWSER_STORAGE_ITEM_NAME+"_ACC", JSON.stringify(user));

            // Also save the new token+account in localStorage
            window.localStorage.setItem(this.BROWSER_STORAGE_ITEM_NAME, token);
            window.localStorage.setItem(this.BROWSER_STORAGE_ITEM_NAME+"_ACC", JSON.stringify(user));
        }
    }

    getTokenFromBrowserStorage() {
        if (this._currentToken != null) return this._currentToken;

        // Try to get the token from sessionStorage
        this._currentToken = window.sessionStorage.getItem(this.BROWSER_STORAGE_ITEM_NAME);
        let jsonAccount = window.sessionStorage.getItem(this.BROWSER_STORAGE_ITEM_NAME + "_ACC");

        if (this._currentToken == null) {
            // Token not found in sessionStorage, try to find it in localStorage
            this._currentToken = window.localStorage.getItem(this.BROWSER_STORAGE_ITEM_NAME);

            if (this._currentToken != null) {
                // If token is found in localStorage, replicate to sessionStorage for this session
                window.sessionStorage.setItem(this.BROWSER_STORAGE_ITEM_NAME, this._currentToken);

                // Try to get the account from localStorage and replicate to sessionStorage
                let jsonAccountLocalStorage = window.localStorage.getItem(this.BROWSER_STORAGE_ITEM_NAME + "_ACC");
                if (jsonAccountLocalStorage != null) {
                    window.sessionStorage.setItem(this.BROWSER_STORAGE_ITEM_NAME + "_ACC", jsonAccountLocalStorage);
                    this._currentUser = JSON.parse(jsonAccountLocalStorage);
                }
            }
        }

        // Parse and set the current account if available
        if (jsonAccount != null) {
            this._currentUser = JSON.parse(jsonAccount);
        }

        // log the recovered token and current account
        console.log("SessionService recovered token: ", this._currentToken);
        console.log("Current User:", this._currentUser);

        return this._currentToken;
    }

    signOut() {
        this.saveTokenIntoBrowserStorage(null, null);
        this._currentUser = null;

        window.localStorage.removeItem(this.BROWSER_STORAGE_ITEM_NAME);
        window.localStorage.removeItem(this.BROWSER_STORAGE_ITEM_NAME + "_ACC");
    }

}