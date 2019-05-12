import React, { Component } from 'react';
import ReactDOM from 'react-dom';
import Swiper from 'react-id-swiper';
import './Home.css';
import 'react-id-swiper/src/styles/css/swiper.css';

class Home extends Component {

    constructor(props) {
        super(props);
    }

    render() {

        const params = {
            direction: 'vertical',
            shouldSwiperUpdate: true,

        };

        const poistList = null;

        return (
            <div className="wrapper">
                <Swiper {...params}>
                    <div>Slide 1</div>
                    <div>Slide 2</div>
                    <div>Slide 3</div>
                    <div>Slide 4</div>
                </Swiper>
            </div>
        )
    }
}

class Post extends Component {

    render() {

    }
}

export default Home;