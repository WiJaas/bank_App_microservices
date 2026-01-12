import axiosClient from "./axiosClient";

// list all clients
export const employeeListClients = async () => {
  const res = await axiosClient.get("/clients");
  return res.data;
};

// create client
export const employeeCreateClient = async (payload) => {
  const res = await axiosClient.post("/clients/create", payload);
  return res.data;
};

export const employeeCreateAccount = async (payload) => {
  const res = await axiosClient.post("/accounts", payload);
  return res.data;
};

// GET accounts by client id
export const employeeGetClientAccounts = async (clientId) => {
  const res = await axiosClient.get(`/accounts/client/${clientId}`);
  return res.data;
};

export const employeeCreateTransaction = async (payload) => {
  const res = await axiosClient.post("/transactions/transfer", {
    sourceAccountId: payload.sourceAccountId,
    destinationAccountId: payload.destinationAccountId,
    amount: payload.amount,
    description: payload.description
  });
  return res.data;
};

export const employeeGetAccountTransactions = async (accountId) => {
  const res = await axiosClient.get(`/transactions/account/${accountId}`);
  return res.data; // array
};


