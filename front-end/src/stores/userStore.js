import {defineStore} from "pinia";

export const useUserStore = defineStore('user', {
    state: () => ({
        user: null,
    }),
    actions: {
        login(user) {
            this.user = user;
        }, logout() {
            this.user = null
        }
    }
})