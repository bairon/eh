package com.eldritch.investigator;

public class Akachi extends InvBase {

    @Override
    public void act() {
        // Посмотрите два верхних жетона в стопке.
        // Один из этих жеронов верните на верх стопки, а другой под низ стопки.

    }

    @Override
    public void ability() {
        // Закрыв врата в ходе контакта в Иных мирах, вы можете переместиться в любую локацию с вратами или уликой
    }

    public void death() {
        // Вы находите Акачи в коме, и врачи не уверены, что она выкарабкается. Возьмите всё имущество Акачи.
        // Медсёстры очень хорошо к ней относятся, и вы пытаетесь разузнать у них, о чём она говорила, пока была в сознании (общение).
        // При успехе вам рассказывают много важного: снизьте безысходность на 1.
        // При провале никто не хочет поделиться её рассказами. В любом случае сбросьте фишку Акачи.
    }

    public void insanity() {
        // Душа Акачи покинула этот мир, и её больше не интересуют материальные блага.
        // Возьмите всё имущество Акачи. Шаман даст вам горький напиток, чтобы вы могли с ней встретиться.
        // Опорожнив сосуд, вы погружаетесь в кошмары, но заставляете себя продолжать поиски Акачи (воля).
        // При успехе её голос рассказывает вам всё, о чём она узнала: снизьте безысходность на 1.
        // При провале вы сворачиваетесь в углу и ждёте, пока действие напитка не пройдет.
        // В любом случае сбросьте фишку Акачи.
    }
}
