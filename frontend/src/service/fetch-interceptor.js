// // see https://www.npmjs.com/package/fetch-intercept
// import fetchIntercept from 'fetch-intercept';
// import {showSuccessfulNotification, showUnsuccessfulNotification} from "@/backend";
//
// export class FetchInterceptor {
//     static theInstance; // the singleton instance that has been registered
//     sessionService;     // the sessionService which tracks the authorisation
//     router;
//     unregister;         // callback function to unregister this instance
//
//     constructor(sessionService, router) {
//         this.sessionService = sessionService;
//         this.router = router;
//
//         FetchInterceptor.theInstance = this;
//         // fetchIntercept does not register the object closure, only the methods as functions
//         this.unregister = fetchIntercept.register(this);
//         console.log("FetchInterceptor has been registered. Current token = ",
//             FetchInterceptor.theInstance.sessionService.currentToken);
//     }
//
//     request(url, options) {
//         let token = FetchInterceptor.theInstance.sessionService.currentToken;
//         console.log("FetchInterceptor request: ", url, options, token);
//
//         if (token == null) {
//             return [url, options];
//         } else if (options == null) {
//             return [url, { headers: { Authorization: token }}]
//         } else {
//             let newOptions = { ...options };
//             Object.assign(newOptions, {headers: { Authorization: token }});
//
//             console.log("FetchInterceptor request: ", url, newOptions);
//             return [url, newOptions];
//         }
//     }
//
//     response(response) {
//
//         if (response.ok) {
//             showSuccessfulNotification();
//         } else if (response.status >= 400 && response.status < 600) {
//             FetchInterceptor.theInstance.handleErrorInResponse(response);
//         }
//         return response;
//     }
//
//     requestError(error) {
//         // Called when an error occurred during another 'request' interceptor call
//         console.log("FetchInterceptor requestError: ", error);
//         return Promise.reject(error);
//     }
//
//     responseError(error) {
//         // Handle a fetch error
//         console.log("FetchInterceptor responseError: ", error);
//         return Promise.reject(error);
//     }
//
//     async handleErrorInResponse(response) {
//
//         if (!response.ok) {
//             showUnsuccessfulNotification();
//         }
//          if (response.status === 401) {
//             // unauthorised request, redirect to signIn page
//             this.router.push({ path: '/sign-in',});   // vue-router
//         } else if (response.status !== 406) {
//             // 406='Not Acceptable' error is used for logon failure
//             // TODO handle any other error
//         }
//     }
//
//     // tryRecoverNewJWToken(response) {
//     //     // TODO check the response on availability of a JWT
//     //     //  and request the session service to save that
//     // }
// }