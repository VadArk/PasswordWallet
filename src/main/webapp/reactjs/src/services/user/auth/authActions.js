import {LOGIN_REQUEST, LOGOUT_REQUEST, SUCCESS, FAILURE} from './authTypes';
import axios from "axios";

export const authenticateUser = (login, password, mode) => {
    return dispatch => {
        dispatch(loginRequest());
        localStorage.setItem('masterPassword', password);
        localStorage.setItem('login', login);
        localStorage.setItem('mode', mode);
        const user = {
            login: login,
            password: password
        };


        axios.post("http://localhost:8080/api/v1/user/login", user)
            .then((response) => {

                if (response.data.status === "0") {
                    dispatch(success(response.data.status));
                } else
              {
                    dispatch(failure(response.data));}

            });
    };
};

const loginRequest = () => {
    return {
        type: LOGIN_REQUEST
    };
};
export const logoutUser = () => {
    return dispatch => {

        localStorage.removeItem('login')
        localStorage.removeItem('masterPassword')
        dispatch(logoutRequest());
        dispatch(success("1"));
    };
};
const logoutRequest = () => {
    return {
        type: LOGOUT_REQUEST
    };
};

const success = isLoggedIn => {
    return {
        type: SUCCESS,
        payload: isLoggedIn
    };
};
const failure = loginResponse => {
    return {
        type: FAILURE,
        payload: {status:loginResponse.status,
            lastSuccess: loginResponse.lastSuccess,
            lastUnsuccessful: loginResponse.lastUnsuccessful
        }
    };
};

