import axios, {  AxiosError } from "axios";
import { TAuthState } from "../context/AuthContext";
import {  errorHandler } from "./handleError";
import { TUser } from "../types/User";


//Axios instance
let instance = axios.create({
    baseURL:"/api/users",
    timeout: 1000,
    headers: {"duvi":"duvivalue"}
})

//Custom type
type TUserData = {
    username: string,
    email: string,
    password: string
}



//Sign Up User
export async function signUpUser( userData: TUserData, asAdmin:boolean=false): Promise<TAuthState> {

    const { username, email, password } = userData; 
    const signUpData = {
        username: username, 
        email: email, 
        password: password, 
        role: asAdmin? "ADMIN" : "USER" 
    };
    
    try {
        const { data } = await instance.request({
            data: signUpData,
            method: "POST",
            url:"/register"
        })
        const { token, loggedUser } : { token: string, loggedUser: TUser }= data;
        const headers = {Authorization: `Bearer ${token}`};
        const loggedIn = { headers: headers, isAuth: true, loggedUser: loggedUser };
        
        localStorage.setItem("loggedUser", JSON.stringify(loggedIn));

        return loggedIn;

    } catch (error: any) {
        errorHandler(error);
        throw(error);
    }
    
}
//Login User
export async function loginUser({ login, password } : { login: string, password: string }): Promise<TAuthState>  {

    try {
        const { data } = await instance.request({
            data: {login: login, password: password},
            method: "POST",
            url: "/login"
        });
        
        const headers = { "Authorization" : "Bearer "+data.token};
        const loggedIn = { headers: headers, isAuth: true, loggedUser: data.loggedUser }

        localStorage.setItem("loggedUser", JSON.stringify(loggedIn));
        return loggedIn;
    } catch (error: any) {
        errorHandler(error);
        throw(error);
    }

}
//Logout User
export function logoutUser() {
    localStorage.removeItem("loggedUser");
    return {
        headers: null,
        isAuth: false,
        loggedUser: {
            bio: null,
            email: "",
            image: null,
            token: "",
            username: "",
        }
    }
}


//Get current User
export async function getUser({ headers } : { headers: object }) : Promise<TUser> {

    try {
        const { data } = await axios.request({
            method: "GET",
            url: "/login", 
            headers: headers
        });
        return data.loggedUser;
    } catch (error) {
        errorHandler(error as AxiosError);
        throw(error);
    }
}

//Get  User By Username
export async function getUserByUsername({ headers, username } : { 
    headers: object, 
    username: string }) : Promise<TUser> {

    try {
        const { data } = await axios.request({
            method: "GET",
            url: `/${username}`, 
            headers: headers
        });
        return data;
    } catch (error) {
        errorHandler(error as AxiosError);
        throw(error);
    }
}



