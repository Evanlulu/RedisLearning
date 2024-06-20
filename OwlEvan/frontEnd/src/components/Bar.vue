<template>
  <v-navigation-drawer
    class="menubar"
    expand-on-hover
    rail
    theme="dark"
    :width="232"
    :rail-width="80"
  >
    <v-list density="compact" nav class="py-5">
      <v-list-item
        prepend-icon="mdi-home"
        title="首頁"
        value="myfiles"
        rounded="xl"
        class="mb-10"
        @click="goToDashBoard"
      ></v-list-item>
      <v-list-item
        prepend-icon="mdi-chart-bar"
        title="訂單查詢"
        value="shared"
        rounded="xl"
        @click="goToTable"
      ></v-list-item>
    </v-list>
    <template v-slot:append>
      <v-list density="compact" nav>
        <v-list-item
          prepend-icon="mdi-account"
          title="登出"
          value="logout"
          append-icon="mdi-logout"
          rounded="xl"
          class="mb-10"
          @click="goToLogin"
        ></v-list-item>
      </v-list>
    </template>
  </v-navigation-drawer>
</template>
  
  <script>
import { useRouter } from "vue-router";
export default {
  setup() {
    const router = useRouter();

    const goToLogin = () => {
      router.push("/");
    };
    const goToDashBoard = () => {
      router.push("/dashboard");
    };
    const goToTable = () => {
      router.push("/table");
    };

    return {
      goToLogin,
      goToDashBoard,
      goToTable
    };
  },
};
</script>
  
  <style scoped>
.menubar {
  background-color: #26221f;
  padding: 10px;
}
.custom-item {
  margin-bottom: 20px;
}
</style>
  