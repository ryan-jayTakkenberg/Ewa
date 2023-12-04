import { Bank } from "@/models/Bank";

export class BankAdaptor {
    url;   // the URL of the endpoint
    constructor() {
        this.url = process.env.VUE_APP_API_URL + '/rest';
    }
    async fetchJson(url, options = null) {
        let response = await fetch(url + '/banks', options)
        if (response.ok) {
            return await response.json();
        } else {
            throw new Error('invalid response ' + response);
        }
    }

    async asyncFindAll() {
        const banksData = await this.fetchJson(this.url);
        return banksData?.map(Bank.copyConstructor);
    }

}