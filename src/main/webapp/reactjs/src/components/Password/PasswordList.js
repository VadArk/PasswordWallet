import React, {Component} from 'react';
import Modal from 'react-awesome-modal';
import {connect} from 'react-redux';
import {showPassword} from '../../services/index';

import './../../assets/css/Style.css';
import {Card, Table, Image, ButtonGroup, Button, InputGroup, FormControl} from 'react-bootstrap';
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';
import {
    faList
} from '@fortawesome/free-solid-svg-icons';
import {Link} from 'react-router-dom';
import MyToast from '../MyToast';
import axios from 'axios';

class PasswordList extends Component {

    constructor(props) {
        super(props);
        this.state = {
            passwords: [],
            search: '',
            currentPage: 1,
            passwordsPerPage: 10,
            sortDir: "asc",
            visible: false,
            share: false,
            sharedLogin: "",
            mode: localStorage.getItem("mode"),
            isAlertModeVisible: false
        };
    }

    sortData = () => {
        setTimeout(() => {
            this.state.sortDir === "asc" ? this.setState({sortDir: "desc"}) : this.setState({sortDir: "asc"});
            this.findAllPasswords(this.state.currentPage);
        }, 500);
    };

    componentDidMount() {
        this.findAllPasswords(this.state.currentPage);
    }


    findAllPasswords(currentPage) {

        currentPage -= 1;
        axios.get("http://localhost:8080/api/v1/password/allbylogin?login=" + localStorage.getItem('login') + "&pageNumber=" + currentPage + "&pageSize=" + this.state.passwordsPerPage + "&sortBy=price&sortDir=" + this.state.sortDir)
            .then(response => response.data)
            .then((data) => {
                this.setState({
                    passwords: data.content.map(password => {
                        password.password = "******"
                        return password
                    }),
                    totalPages: data.totalPages,
                    totalElements: data.totalElements,
                    currentPage: data.number + 1
                });
            });
    };

    deletePassword = (passwordId) => {
        if (this.state.mode === 'write') {


            axios.put("http://localhost:8080/api/v1/password/delete?passwordId=" + passwordId + "&userLogin=" + localStorage.getItem('login'))
                .then((response) => {

                    console.log(response)

                    if (response.data) {
                        if (response) {
                            this.setState({"show": true});
                            setTimeout(() => this.setState({"show": false}), 3000);
                            this.setState({
                                passwords: this.state.passwords.filter(password => password.id !== passwordId)
                            });
                        } else {
                            this.setState({"show": false});
                        }
                    } else {alert("You cannot delete shared password!")}
                });
        } else {
            alert("You can`t delete, because it is Read Mode!")
            this.setState({"isAlertModeVisible": true});
            setTimeout(() => this.setState({"isAlertModeVisible": false}), 4000);

        }

    };

    sharePassword = passwordId => {

        const share = {
            userLogin: localStorage.getItem('login'),
            passwordId: passwordId,
            sharedLogin: this.state.sharedLogin
        };

        axios.post("http://localhost:8080/api/v1/password/share", share)
            .then(response => {
                    if (response.data === true) {

                        this.setState({share: true});

                        setTimeout(() => this.setState({"share": false}), 3000);
                    } else {
                        this.setState({"share": false});
                        alert("You have to be an owner to share password!")
                    }
                }
            );

    };
    editPasswordHandler = e => {
        if (this.state.mode === 'read') {
            e.preventDefault();

            alert("Modifying is not allowed, because it is Read Mode!")

        }

    };

    historyPasswordHandler = e => {

    };



    showPassword = passwordId => {

        const show = {
            masterPassword: localStorage.getItem('masterPassword'),
            passwordId: passwordId
        };
        axios.post("http://localhost:8080/api/v1/password/show", show)
            .then(response => {

                    console.log(response.data)
                    if (response.data != null) {
                        //console.log(this)
                        setTimeout(() => this.setState(
                            {
                                passwords: this.state.passwords.map(password => {

                                    if (password.id === passwordId) {
                                        password.password = response.data
                                    }
                                    return password

                                })
                            }), 0);
                        setTimeout(() => this.setState({
                                passwords: this.state.passwords.map(password => {

                                    if (password.id === passwordId) {
                                        password.password = "******"
                                    }
                                    return password

                                })
                            }
                        ), 3000);

                    } else {
                        this.setState({"show": false});
                    }
                }
            );

    };

    changePage = event => {
        let targetPage = parseInt(event.target.value);
        if (this.state.search) {
            this.searchData(targetPage);
        } else {
            this.findAllPasswords(targetPage);
        }
        this.setState({
            [event.target.name]: targetPage
        });
    };

    firstPage = () => {
        let firstPage = 1;
        if (this.state.currentPage > firstPage) {
            if (this.state.search) {
                this.searchData(firstPage);
            } else {
                this.findAllPasswords(firstPage);
            }
        }
    };

    prevPage = () => {
        let prevPage = 1;
        if (this.state.currentPage > prevPage) {
            if (this.state.search) {
                this.searchData(this.state.currentPage - prevPage);
            } else {
                this.findAllPasswords(this.state.currentPage - prevPage);
            }
        }
    };

    lastPage = () => {
        let condition = Math.ceil(this.state.totalElements / this.state.passwordsPerPage);
        if (this.state.currentPage < condition) {
            if (this.state.search) {
                this.searchData(condition);
            } else {
                this.findAllPasswords(condition);
            }
        }
    };

    nextPage = () => {
        if (this.state.currentPage < Math.ceil(this.state.totalElements / this.state.passwordsPerPage)) {
            if (this.state.search) {
                this.searchData(this.state.currentPage + 1);
            } else {
                this.findAllPasswords(this.state.currentPage + 1);
            }
        }
    };

    searchChange = event => {
        this.setState({
            [event.target.name]: event.target.value
        });
    };

    cancelSearch = () => {
        this.setState({"search": ''});
        this.findAllPasswords(this.state.currentPage);
    };

    searchData = (currentPage) => {
        currentPage -= 1;
        axios.get("http://localhost:8081/rest/passwords/search/" + this.state.search + "?page=" + currentPage + "&size=" + this.state.passwordsPerPage)
            .then(response => response.data)
            .then((data) => {
                this.setState({
                    passwords: data.content,
                    totalPages: data.totalPages,
                    totalElements: data.totalElements,
                    currentPage: data.number + 1
                });
            });
    };

    openModal() {
        this.setState({
            visible: true
        });
    }

    closeModal() {
        this.setState({
            visible: false

        });
    }

    changeUserLoginInput = event => {
        this.setState({
            sharedLogin: event.target.value
        });
    };

    render() {
        const {passwords, currentPage, totalPages, search, sharedLogin, share, mode, isAlertModeVisible} = this.state;

        return (
            <div>

                <Card className={"border border-light bg-light text-black"}>
                    <Card.Header>
                        <div style={{"float": "left"}}>
                            Password List
                        </div>
                    </Card.Header>
                    <Card.Body>
                        <Table bordered hover striped variant="black">
                            <thead>
                            <tr>
                                <th>Login</th>
                                <th>Password</th>
                                <th>URL</th>
                                <th>Description</th>
                                <th>Is yours</th>
                                <th>Activity</th>
                            </tr>
                            </thead>
                            <tbody>
                            {
                                passwords.length === 0 ?
                                    <tr align="center">
                                        <td colSpan="7">No passwords Available.</td>
                                    </tr> :
                                    passwords.map((password) => (
                                        <tr key={password.id}>

                                            <td>{password.login} </td>
                                            <td>{password.password}</td>
                                            <td>{password.web_address}</td>
                                            <td>{password.description}</td>
                                            <td>{password.ownerId != null? 'no' : 'yes' }</td>


                                            <td>
                                                <ButtonGroup>


                                                    <Button size="sm" variant="outline-primary"
                                                            onClick={this.openModal.bind(this, password.id)}>Share</Button>

                                                    <Modal visible={this.state.visible} width="400" height="300"
                                                           effect="fadeInUp" onClickAway={() => this.closeModal()}>
                                                        <div style={{padding: '25px'}}>
                                                        <div style={{"display": share ? "block" : "none"}}>
                                                            <MyToast
                                                                children={{show: share, message: "Successfully."}}/>
                                                        </div>
                                                        <div>
                                                            <h1 style={{color: 'black'}}>Share password </h1>
                                                            <p style={{color: 'black'}}>Type an email address or
                                                                login</p>
                                                            <form class="form">
                                                                <label for="name" class="label-name"
                                                                       style={{color: 'black',padding: '5px'}}>User login: </label>

                                                                <input type="text" value={sharedLogin} id="name"
                                                                       name="name" maxlength="40"
                                                                       class="field field-name"
                                                                       onChange={this.changeUserLoginInput}/>

                                                            </form>

                                                            <a href="javascript:void(0);"
                                                               onClick={() => this.sharePassword(password.id)}>Share                     </a>
                                                            <a href="javascript:void(0);"
                                                               onClick={() => this.closeModal()}>              Close</a>
                                                        </div>
                                                        </div>

                                                    </Modal>

                                                    <Button size="sm" variant="outline-primary"
                                                            onClick={this.showPassword.bind(this, password.id)}>Show</Button>


                                                    {<Link
                                                        onClick={(e) => this.editPasswordHandler(e)}
                                                        to={"edit/" + password.id}
                                                        className="btn btn-sm btn-outline-danger">Edit</Link>}

                                                    <Button size="sm" variant="outline-danger"
                                                            onClick={this.deletePassword.bind(this, password.id)}>Delete</Button>

                                                    {<Link
                                                        onClick={(e) => this.historyPasswordHandler(e)}
                                                        to={"history/" + password.id}
                                                        className="btn btn-sm btn-outline-danger">History</Link>}

                                                </ButtonGroup>
                                            </td>
                                        </tr>
                                    ))
                            }
                            </tbody>
                        </Table>
                    </Card.Body>
                    {passwords.length > 0 ?
                        <Card.Footer>
                            <div style={{"float": "left"}}>
                                Showing Page {currentPage} of {totalPages}
                            </div>
                            <div style={{"float": "right"}}>

                            </div>
                        </Card.Footer> : null
                    }
                </Card>
            </div>
        );
    }
}

const mapStateToProps = state => {
    return {
        passwordObject: state.password
    };
};

const mapDispatchToProps = dispatch => {
    return {
        showPassword: (passwordId) => dispatch(showPassword(passwordId))
    };
};

export default connect(mapStateToProps, mapDispatchToProps)(PasswordList);