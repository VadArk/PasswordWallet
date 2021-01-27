import React, {Component} from 'react';

import {Navbar, Container, Col} from 'react-bootstrap';

export default class Footer extends Component {
    render() {
        let fullYear = new Date().getFullYear();

        return (
            <Navbar fixed="bottom" bg="light" variant="light">
                <Container>
                    <Col lg={12} className="text-center text-muted">
                        <div>{fullYear} BSI</div>
                    </Col>
                </Container>
            </Navbar>
        );
    }
}