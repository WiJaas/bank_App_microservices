import axiosClient from "./axiosClient";


export const getMyClientProfile = async () => {
  const res = await axiosClient.get("/clients/me");
  return res.data;
};

// Client views their accounts
export const clientGetAccounts = async () => {
    const res = await axiosClient.get("/accounts/me");
    return res.data;
};

// Client views own operations
export const clientGetMyOperations = async () => {
    const res = await axiosClient.get("/operations/me");
    return res.data;
};

// Client initiates transfer between own accounts / beneficiaries
export const clientDoTransfer = async (payload) => {
    const res = await axiosClient.post("/operations/me/transfer", payload);
    return res.data;
};


// get transactions of account
export const clientGetAccountTransactions = async (accountId) => {
    const res = await axiosClient.get(`/transactions/account/${accountId}`);
    return res.data;
};

