<template>
  <v-container class="login-container" style="width: 430px" align="center">
    <v-row>
      <v-col>
        <div class="welcome">歡迎光臨!</div>
        <form @submit.prevent="handleLogin">
          <div class="input-group">
            <input
              type="text"
              id="username"
              v-model="username"
              placeholder="請輸入帳號"
              required
            />
          </div>
          <div class="input-group">
            <input
              type="password"
              id="password"
              v-model="password"
              placeholder="請輸入密碼"
              required
            />
          </div>
          <button type="submit">登入</button>
        </form>
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup>
import { ref } from "vue";
import { inject } from "vue";

const username = ref("");
const password = ref("");
const router = inject("router");

const handleLogin = () => {
  console.log("Username:", username.value);
  console.log("Password:", password.value);
  router.push("/dashboard");
};
</script>

<style scoped>
.login-container {
  padding-top: 15%;
  width: 430px;
  /* margin: 0 auto; */
  text-align: center;
}

.welcome {
  font-size: 32px;
  color: #26221f;
  margin-bottom: 20px;
}

.input-group {
  margin-bottom: 20px;
  text-align: left;
}

input {
  width: 100%;
  padding: 10px;
  box-sizing: border-box;
  border: 1px solid #ccc;
  border-radius: 4px;
}

input::placeholder {
  color: #888;
}

button {
  width: 100%;
  padding: 10px;
  background-color: #ccbb9a;
  border: none;
  color: white;
  font-size: 16px;
  cursor: pointer;
  transition: background-color 0.3s;
  border-radius: 4px;
}

button:hover {
  background-color: #b3a588;
}
</style>
