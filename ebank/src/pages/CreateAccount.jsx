import React, { useState } from "react";
import { employeeCreateAccount } from "../services/employeeService";

const CreateAccount = () => {
    const [clientId, setClientId] = useState("");
    const [initialBalance, setInitialBalance] = useState("");
    const [accountType, setAccountType] = useState("CURRENT");
    const [message, setMessage] = useState("");

    const handleSubmit = async (e) => {
        e.preventDefault();

        try {
            const payload = {
                clientId: Number(clientId),
                initialBalance: Number(initialBalance),
                accountType: accountType
            };

            await employeeCreateAccount(payload);
            setMessage("Account successfully created");
            setClientId("");
            setInitialBalance("");
        } catch (err) {
            setMessage("Error creating account");
        }
    };

    return (
        <div style={{ maxWidth: "600px", margin: "0 auto" }}>
            <h2>Create Bank Account</h2>

            {message && <p>{message}</p>}

            <form onSubmit={handleSubmit}>
                <div>
                    <label>Client ID</label>
                    <input
                        type="number"
                        value={clientId}
                        onChange={(e) => setClientId(e.target.value)}
                        required
                    />
                </div>

                <div>
                    <label>Initial Balance</label>
                    <input
                        type="number"
                        value={initialBalance}
                        onChange={(e) => setInitialBalance(e.target.value)}
                        required
                    />
                </div>

                <div>
                    <label>Account Type</label>
                    <select
                        value={accountType}
                        onChange={(e) => setAccountType(e.target.value)}
                    >
                        <option value="CURRENT">CURRENT</option>
                        <option value="SAVINGS">SAVINGS</option>
                    </select>
                </div>

                <button type="submit">Create Account</button>
            </form>
        </div>
    );
};

export default CreateAccount;
